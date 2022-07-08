package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CifradoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cifrado.class);
        Cifrado cifrado1 = new Cifrado();
        cifrado1.setId(1L);
        Cifrado cifrado2 = new Cifrado();
        cifrado2.setId(cifrado1.getId());
        assertThat(cifrado1).isEqualTo(cifrado2);
        cifrado2.setId(2L);
        assertThat(cifrado1).isNotEqualTo(cifrado2);
        cifrado1.setId(null);
        assertThat(cifrado1).isNotEqualTo(cifrado2);
    }
}
