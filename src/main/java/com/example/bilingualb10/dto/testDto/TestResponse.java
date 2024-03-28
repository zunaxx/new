package com.example.bilingualb10.dto.testDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class TestResponse {
     Long id;
     String title;
     String description;
     Boolean enable;
     Integer duration;

     public TestResponse(Long id, String title, String description, Boolean enable , Integer duration) {
          this.id = id;
          this.title = title;
          this.description = description;
          this.enable = enable;
          this.duration = duration;
     }
}