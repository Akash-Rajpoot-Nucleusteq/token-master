package in.nucleusteq.plasma.utils;

import in.nucleusteq.plasma.entity.Role;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Data
@EqualsAndHashCode
@Service
public class RoleUtils {

    public String getHighestWeightRole(Set<Role> set) {
        String highestWeightRole = null;

        Map<String, Integer> roleWeights = new HashMap<>();
        roleWeights.put("SUPER ADMIN", 4);
        roleWeights.put("ADMIN", 3);
        roleWeights.put("MANAGER", 2);
        roleWeights.put("EMPLOYEE", 1);

        int maxWeight = Integer.MIN_VALUE;

        for (Role role : set) {
            String roleName = role.getName();
            if (roleWeights.containsKey(roleName)) {
                int weight = roleWeights.get(roleName);
                if (weight > maxWeight) {
                    maxWeight = weight;
                    highestWeightRole = roleName;
                }
            }
        }
        return highestWeightRole;
    }
}
