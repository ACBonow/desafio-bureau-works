package com.arthur.translatormanager.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Translator {
    // Arthur Bonow - 21-04-2025
    // Português: Modelo para representar um tradutor
    // English: Model to represent a translator
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String sourceLanguage;
    private String targetLanguage;
    // Arthur Bonow - 21-04-2025
    // Português: gerenciar o relacionamento entre o documento e o tradutor
    // English: manage the relationship between the document and the translator
    @OneToMany(mappedBy = "translator")
    private List<Document> documents;
}