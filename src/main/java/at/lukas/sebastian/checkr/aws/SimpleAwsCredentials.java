package at.lukas.sebastian.checkr.aws;

import software.amazon.awssdk.auth.credentials.AwsCredentials;

public class SimpleAwsCredentials implements AwsCredentials {

    private final String accessKeyId;

    private final String secretAccessKey;

    SimpleAwsCredentials(String accessKeyId, String secretAccessKey) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
    }

    @Override
    public String accessKeyId() {
        return accessKeyId;
    }

    @Override
    public String secretAccessKey() {
        return secretAccessKey;
    }
}
