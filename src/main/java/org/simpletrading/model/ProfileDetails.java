package org.simpletrading.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="user_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDetails {

    @Id
    private Long id;
    private String firstName;
    private String secondName;
    private Date birthday;
    private String country;
    private String profileDescription;

    @OneToOne
    @MapsId
    @JoinColumn
    @JsonManagedReference
    private User user;
}
