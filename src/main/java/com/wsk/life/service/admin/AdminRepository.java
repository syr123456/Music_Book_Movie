package com.wsk.life.service.admin;

import com.wsk.life.bean.admin.AdmininformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @DESCRIPTION :
 * @AUTHOR : WuShukai1103
 * @TIME : 2018/3/8  15:32
 */
public interface AdminRepository extends JpaRepository<AdmininformationEntity, Integer>{
    AdmininformationEntity findAdmininformationEntityByPhone(String phone);
}
