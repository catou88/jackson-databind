package tools.jackson.failing;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import tools.jackson.databind.*;

import com.fasterxml.jackson.annotation.PropertyAccessor;

public class RequireSetterForGetter736Test extends BaseMapTest
{
    public static class DataB {
        private int readonly;
        private int readwrite;

        public DataB() {
            readonly = 1;
            readwrite = 2;
        }

        public int getReadwrite() {
            return readwrite;
        }
        public void setReadwrite(int readwrite) {
            this.readwrite = readwrite;
        }
        public int getReadonly() {
            return readonly;
        }
    }

    // for [databind#736]
    public void testNeedForSetters() throws Exception
    {
        ObjectMapper mapper = jsonMapperBuilder()
                .changeDefaultVisibility(vc -> vc
                        .withVisibility(PropertyAccessor.ALL, Visibility.NONE)
                        .withVisibility(PropertyAccessor.FIELD, Visibility.NONE)
                        .withVisibility(PropertyAccessor.GETTER, Visibility.PUBLIC_ONLY)
                        .withVisibility(PropertyAccessor.SETTER, Visibility.PUBLIC_ONLY)
                )
                .enable(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS)
                .build();
        DataB dataB = new DataB();

        String json = mapper.writeValueAsString(dataB);
        assertEquals(a2q("{'readwrite':2}"), json);
    }
}