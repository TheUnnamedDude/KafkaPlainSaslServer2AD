package no.nav.common.security.ldap

import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should be true`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import no.nav.common.security.common.InMemoryLDAPServer
import no.nav.common.security.common.JAASContext

object LDAPAuthorizationSpec : Spek({

    // set the JAAS config in order to do successful init of LDAPAuthorization
    JAASContext.setUp()

    describe("LDAPAuthorization class test specifications") {

        beforeGroup {
            InMemoryLDAPServer.start()
            LDAPCache.invalidateAllGroups()
        }

        given("Classpath to  YAML config - verification of membership") {

            on("user and membership group") {
                it("should return true") {
                    val ldap = LDAPAuthorization.init()
                    ldap.isUserMemberOfAny("bdoe", listOf("ktACons")).`should be true`()
                }
            }
            on("user and non-membership group") {
                it("should return false") {
                    val ldap = LDAPAuthorization.init()
                    ldap.isUserMemberOfAny("adoe", listOf("ktACons")).`should be false`()
                }
            }
            on("user and membership group") {
                it("should return true") {
                    val ldap = LDAPAuthorization.init()
                    ldap.isUserMemberOfAny("adoe", listOf("ktAProd")).`should be true`()
                }
            }
            on("invalid user and existing group") {
                it("should return false") {
                    val ldap = LDAPAuthorization.init()
                    ldap.isUserMemberOfAny("invalid", listOf("ktACons")).`should be false`()
                }
            }
            on("existing user and invalid group") {
                it("should return false") {
                    val ldap = LDAPAuthorization.init()
                    ldap.isUserMemberOfAny("bdoe", listOf("invalid")).`should be false`()
                }
            }
            on("user and {invalid group,membership group}") {
                it("should return true") {
                    val ldap = LDAPAuthorization.init()
                    ldap.isUserMemberOfAny("bdoe", listOf("invalid","ktACons")).`should be true`()
                }
            }
            on("user and {non-membership group,membership group}") {
                it("should return true") {
                    val ldap = LDAPAuthorization.init()
                    ldap.isUserMemberOfAny("bdoe", listOf("ktAProd","ktACons")).`should be true`()
                }
            }
            on("user and {non-membership group,invalid group}") {
                it("should return false") {
                    val ldap = LDAPAuthorization.init()
                    ldap.isUserMemberOfAny("bdoe", listOf("ktAProd","invalid")).`should be false`()
                }
            }
        }

        given("YAML config with root grpBaseDN  - verification of membership") {

            val root = "src/test/resources/adcRootgrpBaseDN.yaml"

            on("user and membership group") {
                it("should return true") {
                    val ldap = LDAPAuthorization.init(root)
                    ldap.isUserMemberOfAny("bdoe", listOf("ktACons")).`should be true`()
                }
            }
            on("user and non-membership group") {
                it("should return false") {
                    val ldap = LDAPAuthorization.init(root)
                    ldap.isUserMemberOfAny("adoe", listOf("ktACons")).`should be false`()
                }
            }
            on("user and membership group") {
                it("should return true") {
                    val ldap = LDAPAuthorization.init(root)
                    ldap.isUserMemberOfAny("adoe", listOf("ktAProd")).`should be true`()
                }
            }
            on("invalid user and existing group") {
                it("should return false") {
                    val ldap = LDAPAuthorization.init(root)
                    ldap.isUserMemberOfAny("invalid", listOf("ktACons")).`should be false`()
                }
            }
            on("existing user and invalid group") {
                it("should return false") {
                    val ldap = LDAPAuthorization.init(root)
                    ldap.isUserMemberOfAny("bdoe", listOf("invalid")).`should be false`()
                }
            }
            on("user and {invalid group,membership group}") {
                it("should return true") {
                    val ldap = LDAPAuthorization.init(root)
                    ldap.isUserMemberOfAny("bdoe", listOf("invalid","ktACons")).`should be true`()
                }
            }
            on("user and {non-membership group,membership group}") {
                it("should return true") {
                    val ldap = LDAPAuthorization.init(root)
                    ldap.isUserMemberOfAny("bdoe", listOf("ktAProd","ktACons")).`should be true`()
                }
            }
            on("user and {non-membership group,invalid group}") {
                it("should return false") {
                    val ldap = LDAPAuthorization.init(root)
                    ldap.isUserMemberOfAny("bdoe", listOf("ktAProd","invalid")).`should be false`()
                }
            }
        }

        // all cases with grpBaseDN and the other parameters will return false... not tested

        afterGroup {
            InMemoryLDAPServer.stop()
        }
    }

})