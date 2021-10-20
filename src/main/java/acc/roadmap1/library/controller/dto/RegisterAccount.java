package acc.roadmap1.library.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAccount {

    private String name;

    private String username;

    private String password;
}

