package ufrn.imd.cardeasy.models;

public enum Role {
  OWNER,
  ADMIN,
  MEMBER;

  public boolean hasAccessOf(
    Role role
  ) {
    if(
      this == Role.OWNER
      || role == Role.MEMBER
    ) return true;
    else return this == role;
  };

  public Role nextRole() {
    if(this == Role.MEMBER) return Role.ADMIN;
    else return Role.OWNER;
  };
};
