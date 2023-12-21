package ru.practicum.explore.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;
    @Size(max = 1000)
    @NotBlank
    String text;
    String authorName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;

    public LocalDateTime getCreated() {
        if (created == null) {
            created = LocalDateTime.now();
        }
        return created;
    }

}
