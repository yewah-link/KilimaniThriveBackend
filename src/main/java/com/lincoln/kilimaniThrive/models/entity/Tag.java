package com.lincoln.kilimaniThrive.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Optional: reverse relationship
    @ManyToMany(mappedBy = "tags")
    private List<Blogs> blogs;
}