package urinov.shz.kunuz.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import urinov.shz.kunuz.abstracts.AbctracEntity;

@Setter
@Getter
@Entity
@Table(name = "profile")
public class ProfileEntity extends AbctracEntity {

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String surname;

    @Column(length = 50, unique = true)
    private String email;

    @Column(length = 50, unique = true)
    private String phone;

    @Column(length = 50)
    private String password;

    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    @Enumerated(EnumType.STRING)
    private ProfileRole role;

    @Column(name = "visible")
    private Boolean visible=Boolean.TRUE;

    @Column(name = "photo_id",unique = true)
    private Integer photoId;
}
