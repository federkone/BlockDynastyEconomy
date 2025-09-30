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

import BlockDynasty.Economy.domain.entities.account.Account;

public class TransferResult {
    private final Account from;
    private final Account to;

    public TransferResult(Account from, Account to) {
        this.from = from;
        this.to   = to;
    }
    public Account getFrom() { return from; }
    public Account getTo()   { return to;   }
}
