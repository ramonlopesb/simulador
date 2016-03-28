package info.cloud;

public interface CacheEvictionStrategyIF {
	
	void cacheEvict(double size, CloudImageRepository repository);

}
