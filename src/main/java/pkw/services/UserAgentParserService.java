package pkw.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * Created by Elimas on 2016-03-03.
 */
@Service
public final class UserAgentParserService implements UserAgentStringParser {

    private final UserAgentStringParser parser = UADetectorServiceFactory
            .getCachingAndUpdatingParser();

    private final Cache<String, ReadableUserAgent> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build();

    @Override
    public String getDataVersion() {
        return parser.getDataVersion();
    }

    @Override
    public ReadableUserAgent parse(final String userAgentString) {
        ReadableUserAgent result = cache.getIfPresent(userAgentString);
        if (result == null) {
            result = parser.parse(userAgentString);
            cache.put(userAgentString, result);
        }
        return result;
    }

    @Override
    @PreDestroy
    public void shutdown() {
        parser.shutdown();
    }
}
