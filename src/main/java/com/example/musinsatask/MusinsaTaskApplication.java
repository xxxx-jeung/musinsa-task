package com.example.musinsatask;

import com.example.musinsatask.service.ProductDataInit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PostConstruct;

@Slf4j
@EnableCaching
@SpringBootApplication
@RequiredArgsConstructor
public class MusinsaTaskApplication {
  private final ProductDataInit productDataInit;

  public static void main(String[] args) {
    SpringApplication.run(MusinsaTaskApplication.class, args);
  }

  @PostConstruct
  public void init() {
    log.info(":::: product data init == {} ::::", productDataInit.productDataInit());
  }
}
