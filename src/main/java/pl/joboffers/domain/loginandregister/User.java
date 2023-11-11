package pl.joboffers.domain.loginandregister;

import lombok.Builder;

@Builder
record User(String id,
            String username,
            String password
            ) {
}
