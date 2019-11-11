package cc.ibooker.netlib.listeners;

import cc.ibooker.netlib.dto.ErrorData;

/**
 * 网络请求接口
 *
 * @author 邹峰立
 */
public interface OnModelListener<T> {
    void onStart();

    void onError(ErrorData errorData);

    void onLogin(ErrorData errorData);

    void onCompleted();

    void onNext(T data);
}