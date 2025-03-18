package hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity;

import jakarta.persistence.*;

/**
 * Represents a role assigned to a user.
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Default constructor.
     */
    public Role() {
    }

    /**
     * Constructs a {@code Role} with a specified name.
     *
     * @param name The name of the role (e.g., "ROLE_AUTHOR", "ROLE_READER").
     */
    public Role(String name) {
        this.name = name;
    }

    /**
     * Gets the role ID.
     *
     * @return The ID of the role.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the role ID.
     *
     * @param id The ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the role name.
     *
     * @return The name of the role.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the role name.
     *
     * @param name The role name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
