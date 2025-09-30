/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package BlockDynasty.Economy.domain.result;

public class Result<T> {
    private final T value;
    private final String errorMessage;
    private final ErrorCode errorCode;

    private Result(T value, String errorMessage, ErrorCode errorCode) {
        this.value = value;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null, null);
    }

    public static <T> Result<T> success() {
        return new Result<>(null, null, null);
    }

    public static <T> Result<T> failure(String errorMessage, ErrorCode errorCode) {
        return new Result<>(null, errorMessage, errorCode);
    }

    public static <T> Result<T> failure(T value,String errorMessage, ErrorCode errorCode) {
        return new Result<>(value, errorMessage, errorCode);
    }

    public boolean isSuccess() {
        return errorMessage == null;
    }

    public boolean isVoid() {
        return value == null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public T getValue() {
        return value;
    }
}