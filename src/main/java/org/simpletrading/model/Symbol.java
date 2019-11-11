package org.simpletrading.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="supported_symbols")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Symbol {

    @Id
    private String symbol;
    private String exchange;
    private String name;
    private Date date;
    private String type;
    private String iexId;
    private String region;
    private String currency;
    private Boolean isEnabled;

}
