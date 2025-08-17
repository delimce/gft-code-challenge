function fn() {
    var env = karate.env; // get environment variable from system property
    karate.log('karate.env system property was:', env);

    // default environment if no env specified
    if (!env) {
        env = 'dev';
    }

    var config = {
        env: env,
        baseUrl: karate.properties['baseUrl'] || 'http://localhost:8083/v1'
    }

    karate.configure('connectTimeout', 5000);
    karate.configure('readTimeout', 5000);

    return config;
}
