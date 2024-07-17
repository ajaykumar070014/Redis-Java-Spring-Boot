package com.Staff.service;

import com.Staff.model.Staff;
import com.Staff.repository.StaffRepository;
import com.redis.service.RedisCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private RedisCacheService redisCacheService;

    public List<Staff> getAllStaffs() {
        List<Staff> allStaffs = (List<Staff>) redisCacheService.get("allStaffs");
        if (allStaffs == null) {
            allStaffs = staffRepository.findAll();
            updateAllStaffsCache(allStaffs);
        }
        return allStaffs;
    }

    public Staff getStaffById(Long id) {
        List<Staff> allStaffs = (List<Staff>) redisCacheService.get("allStaffs");
        if (allStaffs != null) {
            Optional<Staff> StaffOptional = allStaffs.stream()
                    .filter(staff -> staff.getId().equals(id))
                    .findFirst();
            if (StaffOptional.isPresent()) {
                return StaffOptional.get();
            }
        }
        Optional<Staff> StaffOptional = staffRepository.findById(id);
        if (StaffOptional.isPresent()) {
            Staff staff = StaffOptional.get();
            redisCacheService.set("staff:" + id, staff);
            if (allStaffs != null) {
                allStaffs.add(staff);
                redisCacheService.set("allStaffs", allStaffs);
            }
            return staff;
        } else {
            throw new RuntimeException("Staff not found with id: " + id);
        }
    }


    public Staff createStaff(Staff staff) {
        Staff createdStaff = staffRepository.save(staff);
        redisCacheService.set(String.valueOf(createdStaff.getId()), createdStaff);
        updateAllStaffsCache();
        return createdStaff;
    }

    public Staff updateStaff(Long id, Staff staffDetails) {
        Staff updatedStaff = staffRepository.findById(id)
                .map(staff -> {
                    staff.setName(staffDetails.getName());
                    staff.setEmail(staffDetails.getEmail());
                    staff.setDepartment(staffDetails.getDepartment());
                    return staffRepository.save(staff);
                })
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        redisCacheService.set(String.valueOf(id), updatedStaff);
        updateAllStaffsCache();
        return updatedStaff;
    }

    public void deleteStaff(Long id) {
        staffRepository.deleteById(id);
        redisCacheService.delete(String.valueOf(id));
        updateAllStaffsCache();
    }

    private void updateAllStaffsCache() {
        List<Staff> allStaffs = staffRepository.findAll();
        redisCacheService.set("allStaffs", allStaffs);
    }

    private void updateAllStaffsCache(List<Staff> allStaffs) {
        redisCacheService.set("allStaffs", allStaffs);
    }
}