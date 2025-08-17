package br.inatel.pos.dm111.vfu.persistence.user;

import java.util.List;

public record User(String id, String name, String email, String password, List<String> category, UserType type) {

    public enum UserType {REGULAR, RESTAURANT}
}
