package com.jaypark.sns.model.entity;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import com.jaypark.sns.model.AlarmArgs;
import com.jaypark.sns.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "\"alarm\"", indexes = {
	@Index(name = "user_id_idx", columnList = "user_id")
})
@Getter
@Setter
@TypeDef(name = "jsonb", typeClass= JsonBinaryType.class)
@SQLDelete(sql = "UPDATE \"alarm\" SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class AlarmEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@Enumerated(EnumType.STRING)
	private AlarmType alarmType;

	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	private AlarmArgs args;

	@Column(name = "register_at")
	private Timestamp registerAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "deleted_at")
	private Timestamp DeletedAt;

	@PrePersist
	void registerdAt() {
		this.registerAt = Timestamp.from(Instant.now());
	}

	@PreUpdate
	void updatedAt() {
		this.updatedAt = Timestamp.from(Instant.now());
	}

	public static AlarmEntity of(UserEntity userEntity, AlarmType alarmType, AlarmArgs alarmArgs) {
		AlarmEntity entity = new AlarmEntity();
		entity.setAlarmType(alarmType);
		entity.setArgs(alarmArgs);
		entity.setUser(userEntity);

		return entity;
	}
}
