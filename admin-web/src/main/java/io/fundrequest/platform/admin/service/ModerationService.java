package io.fundrequest.platform.admin.service;

import java.util.List;

public interface ModerationService<T> {

    void approve(Long id);

    List<T> listPending();

    List<T> listFailed();

    void decline(Long id);
}
