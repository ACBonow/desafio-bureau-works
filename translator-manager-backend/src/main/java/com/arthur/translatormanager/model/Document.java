package com.arthur.translatormanager.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Document {
    // Arthur Bonow - 21-04-2025
    // Português: Modelo para representar um documento
    // English: Model to represent a document
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    private String content;
    private String locale;
    private String author;
    // Arthur Bonow - 21-04-2025
    // Português: gerenciar o relacionamento entre o documento e o tradutor
    // English: manage the relationship between the document and the translator
    @ManyToOne
    @JoinColumn(name = "translator_id")
    @NonNull
    private Translator translator;
}