package B612.food.customization.service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static B612.food.customization.service.domain.Obesity.*;
import static B612.food.customization.service.domain.Sex.*;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@ToString(exclude = "physicalInfo")
@Table(name = "USERS")
@NoArgsConstructor(access = PROTECTED)
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String email;
    private String phoneNumber;

    @Embedded
    private LogIn logIn;

    @Embedded
    private Address address;

    @Enumerated
    private Sex sex;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "physical_information_id")
    private PhysicalInformation physicalInfo;

    @Enumerated
    private Obesity obesity;

    public User(String name, Sex sex, String phoneNumber, LogIn login, Address address) {
        this.name = name;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.logIn = login;
        this.address = address;
    }

    public User(String name, Sex sex, String phoneNumber, LogIn login, Address address, String email) {
        this.name = name;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.logIn = login;
        this.address = address;
        this.email = email;
    }

    public User(String name, Sex sex, String phoneNumber, LogIn logIn, Address address, PhysicalInformation physicalInfo) {
        this.name = name;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.logIn = logIn;
        this.address = address;
        setPhysicalInfo(physicalInfo);
    }

    public User(String name, Sex sex, String phoneNumber, LogIn logIn, Address address, String email, PhysicalInformation physicalInfo) {
        this.name = name;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.logIn = logIn;
        this.email = email;
        this.address = address;
        setPhysicalInfo(physicalInfo);
    }

    public void setPhysicalInfo(PhysicalInformation physicalInfo) {
        this.physicalInfo = physicalInfo;
        setObesityBySex();
    }

    /**
     * 비즈니스 로직
     */
    private void setObesityBySex() throws IllegalStateException{
        if (sex == MALE) {
            setObesity(10, 20);
        } else if (sex == FEMALE) {
            setObesity(15, 25);
        } else {
            throw new IllegalStateException("""
                    오류발생
                    발생지점: setObesityBySex(Sex sex)
                    발생이유: 성별이 정해지지 않았습니다.""");
        }
    }

    private void setObesity(int underWeightPoint, int overWeightPoint) {
        if (this.physicalInfo.getBodyFat() == 0) {
            return;
        }

        if (this.physicalInfo.getBodyFat() < underWeightPoint) {
            this.obesity = UNDER;
        } else if (this.physicalInfo.getBodyFat() < overWeightPoint) {
            this.obesity = STANDARD;
        } else {
            this.obesity = OVER;
        }
    }

}
