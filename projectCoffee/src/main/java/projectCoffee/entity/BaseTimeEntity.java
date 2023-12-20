package projectCoffee.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.empty;
import static org.springframework.util.ClassUtils.isPresent;

@EntityListeners(value = {AuditingEntityListener.class})
//Auditing을 적용하기 위한 EntityListeners 어노테이션 추가
@MappedSuperclass //공통 매핑 정보가 필요할때
@Getter @Setter
public abstract class BaseTimeEntity {

    @CreatedDate //엔티티가 생성되어 저장될때 시간을 자동으로 저장
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate //변경할때 시간을 자동으로 저장
    private LocalDateTime updateTime;



}
