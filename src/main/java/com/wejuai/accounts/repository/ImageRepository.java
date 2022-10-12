package com.wejuai.accounts.repository;


import com.wejuai.entity.mysql.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, String> {

    Image findByOssKey(String ossKey);

}
