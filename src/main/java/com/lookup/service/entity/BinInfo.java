package com.lookup.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lookup.service.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Table(name = "bin_info")
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BinInfo extends BaseEntity {

    @Column(name = "bin")
    private String bin;

    @JsonProperty(value = "min_range")
    @Column(name = "min_range")
    private String minRange;

    @JsonProperty(value = "max_range")
    @Column(name = "max_range")
    private String maxRange;

    @JsonProperty(value = "alpha_code")
    @Column(name = "alpha_code")
    private String alphaCode;

    @JsonProperty(value = "bank_name")
    @Column(name = "bank_name")
    private String bankName;

}
