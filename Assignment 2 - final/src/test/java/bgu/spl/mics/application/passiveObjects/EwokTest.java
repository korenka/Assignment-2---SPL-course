package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {
    Ewok ewok;
    @BeforeEach
    void setUp() {
        ewok=new Ewok(0);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void acquire() {
        ewok.available=true;
        ewok.acquire();;
        assertEquals(false,ewok.available);
    }

    @Test
    void release() {
        ewok.available=false;
        ewok.release();;
        assertEquals(true,ewok.available);
    }
}