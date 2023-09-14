package sg.edu.nus.iss.miniprojectserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutDetails {
   
    private Long productId;
    private String name;
    private int quantity;
    
}
