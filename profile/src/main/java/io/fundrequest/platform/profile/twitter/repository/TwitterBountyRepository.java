package io.fundrequest.platform.profile.twitter.repository;

import io.fundrequest.platform.profile.twitter.model.TwitterBounty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TwitterBountyRepository extends JpaRepository<TwitterBounty, Long> {

    @Query("select bounty from TwitterBounty bounty where startDate <= :currentDate and (endDate >= :currentDate or endDate = null)")
    List<TwitterBounty> getActiveTwitterBounties(@Param("currentDate") final Date date);

}
