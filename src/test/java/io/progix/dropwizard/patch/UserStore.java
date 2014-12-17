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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserStore {

    private Pet cat, dog, mouse;
    private User tariq, alli, narmeen;

    public UserStore() {
        this.cat = new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget")));
        this.dog = new Pet(1, 9, new ArrayList<>(Arrays.asList("Jonathan")));
        this.mouse = new Pet(2, 1, new ArrayList<>(Arrays.asList("Rodenty")));

        this.tariq = new User(0, "Tariq", new ArrayList<>(Arrays.asList("tariq@progix.io")),
                new ArrayList<>(Arrays.asList(cat)));
        this.alli = new User(1, "Alli", new ArrayList<>(Arrays.asList("alli@beeb.com")),
                new ArrayList<>(Arrays.asList(cat, dog)));
        this.narmeen = new User(2, "Narmeen", new ArrayList<String>(), new ArrayList<>(Arrays.asList(mouse)));
    }

    public Pet getCat() {
        return cat;
    }

    public Pet getDog() {
        return dog;
    }

    public Pet getMouse() {
        return mouse;
    }

    public User getTariq() {
        return tariq;
    }

    public User getAlli() {
        return alli;
    }

    public User getNarmeen() {
        return narmeen;
    }

    public List<User> getUsers() {
        return new ArrayList<>(Arrays.asList(tariq, alli, narmeen));
    }
}
