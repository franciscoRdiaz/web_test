/*
 * (C) Copyright 2017 OpenVidu (http://openvidu.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.fullteaching.backend.e2e;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import io.github.bonigarcia.SeleniumExtension;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;

/**
 * E2E tests for openvidu-testapp.
 *
 * @author Pablo Fuente (pablo.fuente@urjc.es)
 * @since 1.1.1
 */
@Tag("e2e")
@DisplayName("E2E tests for OpenVidu TestApp")
@ExtendWith(SeleniumExtension.class)
@TestInstance(PER_CLASS)
@RunWith(JUnitPlatform.class)
public class FullTeachingTest {

	static String APP_URL = "https://localhost:5000/";
	static Exception ex = null;
	
	final static Logger log = getLogger(lookup().lookupClass());

	BrowserUser user;

	@BeforeAll()
	static void setupAll() {
		
		String selenium;
		if(System.getenv("ET_PUBLIC_EUS_API") == null) {
			//Outside ElasTest
			ChromeDriverManager.getInstance().setup();
			FirefoxDriverManager.getInstance().setup();	
			selenium = "LOCAL";
		} else {
			selenium = System.getenv("ET_PUBLIC_EUS_API");
			log.info("ET_PUBLIC_API: {}", selenium);
		}
		
		log.info("Using {} Selenium Driver", selenium);
		
		if (System.getenv("ET_SUT_HOST") != null) {
			APP_URL = "https://" + System.getenv("ET_SUT_HOST");
		}
		
		log.info("Using URL {} to connect to openvidu-testapp", APP_URL);

	}

	void setupBrowser(String browser) {
		
		switch (browser) {
			case "chrome":
				this.user = new ChromeUser("TestUser", 50);
				break;
			case "firefox":
				this.user = new FirefoxUser("TestUser", 50);
				break;
			default:
				this.user = new ChromeUser("TestUser", 50);
		}

		user.getDriver().get(APP_URL);
	}

	@AfterEach
	void dispose() {
		user.dispose();
	}

	@Test
	@DisplayName("Test video session")
	public void oneToOneVideoAudioSessionChrome() throws Exception {

		String brow = System.getenv("browser");
		
		if (brow != null && (brow.equals("chrome") || brow.equals("firefox"))) {
			setupBrowser(brow);
		} else {
			setupBrowser("chrome");
		}

		log.info("Test video session");
		
		Thread.sleep(25000);
		
	}

}
