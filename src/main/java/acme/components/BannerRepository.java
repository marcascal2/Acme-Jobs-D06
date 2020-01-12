
package acme.components;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.banners.Banner;
import acme.entities.banners.CommercialBanner;
import acme.entities.banners.NonCommercialBanner;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface BannerRepository extends AbstractRepository {

	@Query("select count(b) from CommercialBanner b")
	int countCommercialBanners();

	@Query("select b from CommercialBanner b")
	List<CommercialBanner> findManyCommercialBanners(PageRequest pageRequest);

	@Query("select count(b) from NonCommercialBanner b")
	int countNonCommercialBanners();

	@Query("select b from NonCommercialBanner b")
	List<NonCommercialBanner> findManyNonCommercialBanners(PageRequest pageRequest);

	default Banner findRandomBanner() {
		Banner result;
		int randomIndex, commercialBannerCount, nonCommercialBannerCount, commercialBannerIndex, nonCommercialBannerIndex;
		ThreadLocalRandom random;
		PageRequest page;
		List<CommercialBanner> listCommercial;
		List<NonCommercialBanner> listNonCommercial;
		List<Banner> list = new ArrayList<Banner>();

		commercialBannerCount = this.countCommercialBanners();
		nonCommercialBannerCount = this.countNonCommercialBanners();

		random = ThreadLocalRandom.current();
		commercialBannerIndex = random.nextInt(0, commercialBannerCount);
		page = PageRequest.of(commercialBannerIndex, 1);
		listCommercial = this.findManyCommercialBanners(page);

		random = ThreadLocalRandom.current();
		nonCommercialBannerIndex = random.nextInt(0, nonCommercialBannerCount);
		page = PageRequest.of(nonCommercialBannerIndex, 1);
		listNonCommercial = this.findManyNonCommercialBanners(page);

		listCommercial.stream().forEach(x -> {
			Banner banner = new Banner();
			banner.setPicture(x.getPicture());
			banner.setSlogan(x.getSlogan());
			banner.setTarget(x.getTarget());
			list.add(banner);
		});

		listNonCommercial.stream().forEach(x -> {
			Banner banner = new Banner();
			banner.setPicture(x.getPicture());
			banner.setSlogan(x.getSlogan());
			banner.setTarget(x.getTarget());
			list.add(banner);
		});

		randomIndex = random.nextInt(0, 2);
		result = list.isEmpty() ? null : list.get(randomIndex);
		return result;
	}

}
