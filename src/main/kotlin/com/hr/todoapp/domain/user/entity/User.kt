package com.hr.todoapp.domain.user.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(

    @Column(name = "name")
    var name: String,

    @Column(name = "email")
    var email: String,

    @Column(name = "pw")
    var pw: String,

    @Column(name = "provider_name")
    var providerName: String? = null,

    @Column(name = "provider_id")
    var providerId: String? = null,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}