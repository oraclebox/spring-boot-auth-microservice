package me.oraclebox.auth.service.model

import org.springframework.data.mongodb.repository.MongoRepository

/**
 *
 * Created by oraclebox@gmail.com on 17/9/2016.
 */
interface AccountRepository extends MongoRepository<Account, String> {
    Account findByUsername(String username);
    Account findByEmail(String email);
    Account findBySocialId(String socialId);
}
