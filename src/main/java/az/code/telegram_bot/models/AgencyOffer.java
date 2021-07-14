package az.code.telegram_bot.models;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "agency_offer")
public class AgencyOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String agencyName;
    String UUID;
    Integer messageId;
    @Transient
    byte[] file;
    String filePath;
}
