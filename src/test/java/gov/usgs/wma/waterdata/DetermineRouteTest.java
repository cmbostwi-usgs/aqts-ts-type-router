package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment=WebEnvironment.NONE)
public class DetermineRouteTest {

	@MockBean
	private JsonDataDao jsonDataDao;
	private DetermineRoute determineRoute;
	private RequestObject request;

	@BeforeEach
	public void beforeEach() {
		determineRoute = new DetermineRoute(jsonDataDao);
		request = new RequestObject();
		request.setId(Long.valueOf(1));
	}

	@Test
	public void notFoundAtAllTest() {
		when(jsonDataDao.getJsonData(anyLong())).thenReturn(null);
		ResultObject result = determineRoute.apply(request);
		assertNotNull(result);
		assertEquals(Long.valueOf(1), result.getId());
		assertEquals(DetermineRoute.OTHER, result.getType());
	}

	@Test
	public void foundGenericTest() {
		when(jsonDataDao.getJsonData(anyLong())).thenReturn(new JsonData());
		ResultObject result = determineRoute.apply(request);
		assertNotNull(result);
		assertEquals(Long.valueOf(1), result.getId());
		assertEquals(DetermineRoute.OTHER, result.getType());
	}

	@Test
	public void foundWrongScriptTest() {
		JsonData jsonData = new JsonData();
		jsonData.setResponseCode(200);
		jsonData.setScriptName("abd");
		when(jsonDataDao.getJsonData(anyLong())).thenReturn(jsonData);
		ResultObject result = determineRoute.apply(request);
		assertNotNull(result);
		assertEquals(Long.valueOf(1), result.getId());
		assertEquals(DetermineRoute.OTHER, result.getType());
	}

	@Test
	public void foundTSDescriptionTest() {
		JsonData jsonData = new JsonData();
		jsonData.setResponseCode(200);
		jsonData.setScriptName(DetermineRoute.GET_TS_DATA);
		jsonData.setServiceName(DetermineRoute.GET_TS_DESCRIPTION_LIST);
		when(jsonDataDao.getJsonData(anyLong())).thenReturn(jsonData);
		ResultObject result = determineRoute.apply(request);
		assertNotNull(result);
		assertEquals(Long.valueOf(1), result.getId());
		assertEquals(DetermineRoute.TS_DESCRIPTION_LIST, result.getType());
	}

	@Test
	public void foundTSCorrectedDataTest() {
		JsonData jsonData = new JsonData();
		jsonData.setResponseCode(200);
		jsonData.setScriptName(DetermineRoute.GET_TS_DATA);
		jsonData.setServiceName(DetermineRoute.GET_TS_CORRECTED_DATA);
		when(jsonDataDao.getJsonData(anyLong())).thenReturn(jsonData);
		ResultObject result = determineRoute.apply(request);
		assertNotNull(result);
		assertEquals(Long.valueOf(1), result.getId());
		assertEquals(DetermineRoute.TS_CORRECTED_DATA, result.getType());
	}

	@Test
	public void foundFieldVisitDataTest() {
		JsonData jsonData = new JsonData();
		jsonData.setResponseCode(200);
		jsonData.setScriptName(DetermineRoute.GET_TS_SITE_VISIT);
		jsonData.setServiceName(DetermineRoute.GET_FIELD_VISIT_DATA_BY_LOCATION);
		when(jsonDataDao.getJsonData(anyLong())).thenReturn(jsonData);
		ResultObject result = determineRoute.apply(request);
		assertNotNull(result);
		assertEquals(Long.valueOf(1), result.getId());
		assertEquals(DetermineRoute.FIELD_VISIT_DATA, result.getType());
	}

	@Test
	public void foundSomethingElseTest() {
		JsonData jsonData = new JsonData();
		jsonData.setResponseCode(200);
		jsonData.setScriptName(DetermineRoute.GET_TS_DATA);
		jsonData.setServiceName("abd");
		when(jsonDataDao.getJsonData(anyLong())).thenReturn(jsonData);
		ResultObject result = determineRoute.apply(request);
		assertNotNull(result);
		assertEquals(Long.valueOf(1), result.getId());
		assertEquals(DetermineRoute.OTHER, result.getType());
	}
}
