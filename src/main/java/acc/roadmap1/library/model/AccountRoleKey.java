package acc.roadmap1.library.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AccountRoleKey implements Serializable {

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "role_id")
    private Long roleId;
}
