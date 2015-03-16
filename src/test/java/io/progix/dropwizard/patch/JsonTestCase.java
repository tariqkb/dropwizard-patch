/*
 * Copyright 2014 Tariq Bugrara
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

package io.progix.dropwizard.patch;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonTestCase {

    @JsonRawValue
    private Object patch;
    @JsonRawValue
    private Object doc;
    @JsonRawValue
    private Object expected;

    private String error;
    private String comment;
    private boolean disabled;

    public JsonTestCase() {
    }

    public String getPatch() {
        return patch == null ? null : patch.toString();
    }

    public String getDoc() {
        return doc == null ? null : doc.toString();
    }

    public String getExpected() {
        return expected == null ? null : expected.toString();
    }

    public String getError() {
        return error;
    }

    public String getComment() {
        return comment;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isErrorCase() {
        return error != null;
    }

    public void setPatch(JsonNode patch) {
        this.patch = patch;
    }

    public void setDoc(JsonNode doc) {
        this.doc = doc;
    }

    public void setExpected(JsonNode expected) {
        this.expected = expected;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return comment;
    }
}
