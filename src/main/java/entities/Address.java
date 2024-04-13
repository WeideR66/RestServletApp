package entities;


import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@Builder
@Jacksonized
public class Address {
    private Long id;
    @NonNull
    private String country;
    @NonNull
    private String city;
    @NonNull
    private String street;
    @NonNull
    private Integer number;
}
