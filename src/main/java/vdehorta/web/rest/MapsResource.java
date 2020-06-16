package vdehorta.web.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vdehorta.bean.dto.NewsCategoryDto;
import vdehorta.service.NewsCategoryService;

import java.util.List;

@RestController
@RequestMapping("/maps")
public class MapsResource {

    public MapsResource() {
    }

    /**
     * {@code GET  /all} : Get the mapbox style json
     * @return he mapbox style json string value
     */
    @GetMapping(value = "/mapbox-style", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMapboxStyle() {
        return "{\n" +
                "  \"version\": 8,\n" +
                "  \"name\": \"base-map\",\n" +
                "  \"metadata\": {\n" +
                "    \"maptiler:copyright\": \"This style was generated on MapTiler Cloud. Usage outside of MapTiler Cloud requires valid OpenMapTiles Production Package: https://openmaptiles.com/production-package/ -- please contact us.\",\n" +
                "    \"openmaptiles:version\": \"3.x\",\n" +
                "    \"maputnik:renderer\": \"mbgljs\"\n" +
                "  },\n" +
                "  \"center\": [5.18017619864122, 46.931943811758316],\n" +
                "  \"zoom\": 6.38665697888361,\n" +
                "  \"bearing\": 0,\n" +
                "  \"pitch\": 0,\n" +
                "  \"sources\": {\n" +
                "    \"maptiler_attribution\": {\n" +
                "      \"type\": \"vector\",\n" +
                "      \"attribution\": \"<a href=\\\"https://www.maptiler.com/copyright/\\\" target=\\\"_blank\\\">&copy; MapTiler</a> <a href=\\\"https://www.openstreetmap.org/copyright\\\" target=\\\"_blank\\\">&copy; OpenStreetMap contributors</a>\"\n" +
                "    },\n" +
                "    \"openmaptiles\": {\n" +
                "      \"type\": \"vector\",\n" +
                "      \"url\": \"https://api.maptiler.com/tiles/v3/tiles.json?key=vRGImi4p2hajjVUCsIAE\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"glyphs\": \"https://api.maptiler.com/fonts/{fontstack}/{range}.pbf?key=vRGImi4p2hajjVUCsIAE\",\n" +
                "  \"layers\": [\n" +
                "    {\n" +
                "      \"id\": \"background\",\n" +
                "      \"type\": \"background\",\n" +
                "      \"paint\": {\"background-color\": \"rgb(232, 231, 225)\"}\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"landuse_residential\",\n" +
                "      \"type\": \"fill\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"landuse\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"Polygon\"],\n" +
                "        [\"in\", \"class\", \"residential\", \"suburb\", \"neighbourhood\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"visibility\": \"visible\"},\n" +
                "      \"paint\": {\"fill-color\": \"rgb(223, 223, 223)\", \"fill-opacity\": 0.7}\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"landcover_grass\",\n" +
                "      \"type\": \"fill\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"landcover\",\n" +
                "      \"filter\": [\"==\", \"class\", \"grass\"],\n" +
                "      \"paint\": {\"fill-color\": \"rgb(182, 227, 132)\", \"fill-opacity\": 0.4}\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"landcover_wood\",\n" +
                "      \"type\": \"fill\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"landcover\",\n" +
                "      \"filter\": [\"==\", \"class\", \"wood\"],\n" +
                "      \"paint\": {\n" +
                "        \"fill-color\": \"rgb(189, 234, 110)\",\n" +
                "        \"fill-opacity\": {\"base\": 1, \"stops\": [[8, 0.6], [22, 1]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"water\",\n" +
                "      \"type\": \"fill\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"water\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"Polygon\"],\n" +
                "        [\"!=\", \"intermittent\", 1],\n" +
                "        [\"!=\", \"brunnel\", \"tunnel\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"visibility\": \"visible\"},\n" +
                "      \"paint\": {\"fill-color\": \"rgb(180, 217, 238)\"}\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"water_intermittent\",\n" +
                "      \"type\": \"fill\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"water\",\n" +
                "      \"filter\": [\"all\", [\"==\", \"$type\", \"Polygon\"], [\"==\", \"intermittent\", 1]],\n" +
                "      \"layout\": {\"visibility\": \"visible\"},\n" +
                "      \"paint\": {\"fill-color\": \"rgb(180, 217, 238)\", \"fill-opacity\": 0.7}\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"landcover_ice-shelf\",\n" +
                "      \"type\": \"fill\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"landcover\",\n" +
                "      \"filter\": [\"==\", \"subclass\", \"ice_shelf\"],\n" +
                "      \"layout\": {\"visibility\": \"visible\"},\n" +
                "      \"paint\": {\"fill-color\": \"hsl(47, 26%, 88%)\", \"fill-opacity\": 0.8}\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"landcover_glacier\",\n" +
                "      \"type\": \"fill\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"landcover\",\n" +
                "      \"filter\": [\"==\", \"subclass\", \"glacier\"],\n" +
                "      \"layout\": {\"visibility\": \"visible\"},\n" +
                "      \"paint\": {\n" +
                "        \"fill-color\": \"hsl(47, 22%, 94%)\",\n" +
                "        \"fill-opacity\": {\"base\": 1, \"stops\": [[0, 1], [8, 0.5]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"landcover_sand\",\n" +
                "      \"type\": \"fill\",\n" +
                "      \"metadata\": {},\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"landcover\",\n" +
                "      \"filter\": [\"all\", [\"in\", \"class\", \"sand\"]],\n" +
                "      \"paint\": {\n" +
                "        \"fill-antialias\": false,\n" +
                "        \"fill-color\": \"rgb(246, 247, 0)\",\n" +
                "        \"fill-opacity\": 0.3\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"waterway_tunnel\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"waterway\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"==\", \"brunnel\", \"tunnel\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"visibility\": \"visible\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"rgb(180, 217, 238)\",\n" +
                "        \"line-dasharray\": [3, 3],\n" +
                "        \"line-gap-width\": {\"stops\": [[12, 0], [20, 6]]},\n" +
                "        \"line-opacity\": 1,\n" +
                "        \"line-width\": {\"base\": 1.4, \"stops\": [[8, 1], [20, 2]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"waterway\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"waterway\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"!in\", \"brunnel\", \"tunnel\", \"bridge\"],\n" +
                "        [\"!=\", \"intermittent\", 1]\n" +
                "      ],\n" +
                "      \"layout\": {\"visibility\": \"visible\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"rgb(180, 217, 238)\",\n" +
                "        \"line-opacity\": 1,\n" +
                "        \"line-width\": {\"base\": 1.4, \"stops\": [[8, 1], [20, 8]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"waterway_intermittent\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"waterway\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"!in\", \"brunnel\", \"tunnel\", \"bridge\"],\n" +
                "        [\"==\", \"intermittent\", 1]\n" +
                "      ],\n" +
                "      \"layout\": {\"visibility\": \"visible\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"rgb(180, 217, 238)\",\n" +
                "        \"line-dasharray\": [2, 1],\n" +
                "        \"line-opacity\": 1,\n" +
                "        \"line-width\": {\"base\": 1.4, \"stops\": [[8, 1], [20, 8]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"tunnel_railway_transit\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"minzoom\": 0,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"==\", \"brunnel\", \"tunnel\"],\n" +
                "        [\"==\", \"class\", \"transit\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"butt\", \"line-join\": \"miter\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"hsl(34, 12%, 66%)\",\n" +
                "        \"line-dasharray\": [3, 3],\n" +
                "        \"line-opacity\": {\"base\": 1, \"stops\": [[11, 0], [16, 1]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"building\",\n" +
                "      \"type\": \"fill\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"building\",\n" +
                "      \"paint\": {\n" +
                "        \"fill-antialias\": true,\n" +
                "        \"fill-color\": \"rgba(222, 211, 190, 1)\",\n" +
                "        \"fill-opacity\": {\"base\": 1, \"stops\": [[13, 0], [15, 1]]},\n" +
                "        \"fill-outline-color\": {\n" +
                "          \"stops\": [\n" +
                "            [15, \"rgba(212, 177, 146, 0)\"],\n" +
                "            [16, \"rgba(212, 177, 146, 0.5)\"]\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"road_area_pier\",\n" +
                "      \"type\": \"fill\",\n" +
                "      \"metadata\": {},\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"filter\": [\"all\", [\"==\", \"$type\", \"Polygon\"], [\"==\", \"class\", \"pier\"]],\n" +
                "      \"layout\": {\"visibility\": \"visible\"},\n" +
                "      \"paint\": {\"fill-antialias\": true, \"fill-color\": \"rgb(232, 231, 225)\"}\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"road_pier\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"metadata\": {},\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"filter\": [\"all\", [\"==\", \"$type\", \"LineString\"], [\"in\", \"class\", \"pier\"]],\n" +
                "      \"layout\": {\"line-cap\": \"round\", \"line-join\": \"round\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"hsl(47, 26%, 88%)\",\n" +
                "        \"line-width\": {\"base\": 1.2, \"stops\": [[15, 1], [17, 4]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"road_bridge_area\",\n" +
                "      \"type\": \"fill\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"Polygon\"],\n" +
                "        [\"in\", \"brunnel\", \"bridge\"]\n" +
                "      ],\n" +
                "      \"layout\": {},\n" +
                "      \"paint\": {\"fill-color\": \"rgb(232, 231, 225)\", \"fill-opacity\": 0.5}\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"road_path\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"in\", \"class\", \"path\", \"track\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"square\", \"line-join\": \"bevel\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"hsl(0, 0%, 97%)\",\n" +
                "        \"line-dasharray\": [1, 1],\n" +
                "        \"line-width\": {\"base\": 1.55, \"stops\": [[4, 0.25], [20, 7]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"road_minor\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"minzoom\": 13,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"in\", \"class\", \"minor\", \"service\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"round\", \"line-join\": \"round\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"hsl(0, 0%, 97%)\",\n" +
                "        \"line-width\": {\"base\": 1.55, \"stops\": [[4, 0.25], [20, 30]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"tunnel_minor\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"==\", \"brunnel\", \"tunnel\"],\n" +
                "        [\"==\", \"class\", \"minor_road\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"butt\", \"line-join\": \"miter\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"#efefef\",\n" +
                "        \"line-dasharray\": [0.36, 0.18],\n" +
                "        \"line-width\": {\"base\": 1.55, \"stops\": [[4, 0.25], [20, 30]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"tunnel_major\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"==\", \"brunnel\", \"tunnel\"],\n" +
                "        [\"in\", \"class\", \"primary\", \"secondary\", \"tertiary\", \"trunk\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"butt\", \"line-join\": \"miter\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"#fff\",\n" +
                "        \"line-dasharray\": [0.28, 0.14],\n" +
                "        \"line-width\": {\"base\": 1.4, \"stops\": [[6, 0.5], [20, 30]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"aeroway_area\",\n" +
                "      \"type\": \"fill\",\n" +
                "      \"metadata\": {\"mapbox:group\": \"1444849345966.4436\"},\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"aeroway\",\n" +
                "      \"minzoom\": 4,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"Polygon\"],\n" +
                "        [\"in\", \"class\", \"runway\", \"taxiway\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"visibility\": \"visible\"},\n" +
                "      \"paint\": {\n" +
                "        \"fill-color\": \"rgba(255, 255, 255, 1)\",\n" +
                "        \"fill-opacity\": {\"base\": 1, \"stops\": [[13, 0], [14, 1]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"aeroway_taxiway\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"metadata\": {\"mapbox:group\": \"1444849345966.4436\"},\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"aeroway\",\n" +
                "      \"minzoom\": 12,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"in\", \"class\", \"taxiway\"],\n" +
                "        [\"==\", \"$type\", \"LineString\"]\n" +
                "      ],\n" +
                "      \"layout\": {\n" +
                "        \"line-cap\": \"round\",\n" +
                "        \"line-join\": \"round\",\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"rgba(255, 255, 255, 1)\",\n" +
                "        \"line-opacity\": 1,\n" +
                "        \"line-width\": {\"base\": 1.5, \"stops\": [[12, 1], [17, 10]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"aeroway_runway\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"metadata\": {\"mapbox:group\": \"1444849345966.4436\"},\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"aeroway\",\n" +
                "      \"minzoom\": 4,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"in\", \"class\", \"runway\"],\n" +
                "        [\"==\", \"$type\", \"LineString\"]\n" +
                "      ],\n" +
                "      \"layout\": {\n" +
                "        \"line-cap\": \"round\",\n" +
                "        \"line-join\": \"round\",\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"rgba(255, 255, 255, 1)\",\n" +
                "        \"line-opacity\": 1,\n" +
                "        \"line-width\": {\"base\": 1.5, \"stops\": [[11, 4], [17, 50]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"road_trunk_primary\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"minzoom\": 9,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"in\", \"class\", \"trunk\", \"primary\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"round\", \"line-join\": \"round\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"#fff\",\n" +
                "        \"line-width\": {\"base\": 1.4, \"stops\": [[6, 0.5], [20, 30]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"road_secondary_tertiary\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"minzoom\": 9,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"in\", \"class\", \"secondary\", \"tertiary\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"round\", \"line-join\": \"round\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"#fff\",\n" +
                "        \"line-width\": {\"base\": 1.4, \"stops\": [[6, 0.5], [20, 20]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"road_major_motorway\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"minzoom\": 6,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"==\", \"class\", \"motorway\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"round\", \"line-join\": \"round\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"hsl(0, 0%, 100%)\",\n" +
                "        \"line-offset\": 0,\n" +
                "        \"line-width\": {\"base\": 1.4, \"stops\": [[8, 1], [16, 10]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"railway_transit\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"class\", \"transit\"],\n" +
                "        [\"!=\", \"brunnel\", \"tunnel\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"visibility\": \"visible\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"hsl(34, 12%, 66%)\",\n" +
                "        \"line-opacity\": {\"base\": 1, \"stops\": [[11, 0], [16, 1]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"railway\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"filter\": [\"==\", \"class\", \"rail\"],\n" +
                "      \"layout\": {\"visibility\": \"visible\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"hsl(34, 12%, 66%)\",\n" +
                "        \"line-opacity\": {\"base\": 1, \"stops\": [[11, 0], [16, 1]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"waterway-bridge-case\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"waterway\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"==\", \"brunnel\", \"bridge\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"butt\", \"line-join\": \"miter\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"#bbbbbb\",\n" +
                "        \"line-gap-width\": {\"base\": 1.55, \"stops\": [[4, 0.25], [20, 30]]},\n" +
                "        \"line-width\": {\"base\": 1.6, \"stops\": [[12, 0.5], [20, 10]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"waterway-bridge\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"waterway\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"==\", \"brunnel\", \"bridge\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"round\", \"line-join\": \"round\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"hsl(205, 56%, 73%)\",\n" +
                "        \"line-width\": {\"base\": 1.55, \"stops\": [[4, 0.25], [20, 30]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"bridge_minor case\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"==\", \"brunnel\", \"bridge\"],\n" +
                "        [\"==\", \"class\", \"minor_road\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"butt\", \"line-join\": \"miter\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"#dedede\",\n" +
                "        \"line-gap-width\": {\"base\": 1.55, \"stops\": [[4, 0.25], [20, 30]]},\n" +
                "        \"line-width\": {\"base\": 1.6, \"stops\": [[12, 0.5], [20, 10]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"bridge_major case\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"==\", \"brunnel\", \"bridge\"],\n" +
                "        [\"in\", \"class\", \"primary\", \"secondary\", \"tertiary\", \"trunk\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"butt\", \"line-join\": \"miter\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"#dedede\",\n" +
                "        \"line-gap-width\": {\"base\": 1.55, \"stops\": [[4, 0.25], [20, 30]]},\n" +
                "        \"line-width\": {\"base\": 1.6, \"stops\": [[12, 0.5], [20, 10]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"bridge_minor\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"==\", \"brunnel\", \"bridge\"],\n" +
                "        [\"==\", \"class\", \"minor_road\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"round\", \"line-join\": \"round\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"#efefef\",\n" +
                "        \"line-width\": {\"base\": 1.55, \"stops\": [[4, 0.25], [20, 30]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"bridge_major\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation\",\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"==\", \"brunnel\", \"bridge\"],\n" +
                "        [\"in\", \"class\", \"primary\", \"secondary\", \"tertiary\", \"trunk\"]\n" +
                "      ],\n" +
                "      \"layout\": {\"line-cap\": \"round\", \"line-join\": \"round\"},\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"#fff\",\n" +
                "        \"line-width\": {\"base\": 1.4, \"stops\": [[6, 0.5], [20, 30]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"admin_sub\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"boundary\",\n" +
                "      \"filter\": [\"in\", \"admin_level\", 4, 6, 8],\n" +
                "      \"layout\": {\"visibility\": \"visible\"},\n" +
                "      \"paint\": {\"line-color\": \"hsla(0, 0%, 60%, 0.5)\", \"line-dasharray\": [2, 1]}\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"admin_country_z0-4\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"boundary\",\n" +
                "      \"minzoom\": 0,\n" +
                "      \"maxzoom\": 5,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"<=\", \"admin_level\", 2],\n" +
                "        [\"==\", \"$type\", \"LineString\"],\n" +
                "        [\"!has\", \"claimed_by\"]\n" +
                "      ],\n" +
                "      \"layout\": {\n" +
                "        \"line-cap\": \"round\",\n" +
                "        \"line-join\": \"round\",\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"hsl(0, 0%, 60%)\",\n" +
                "        \"line-width\": {\"base\": 1.3, \"stops\": [[3, 0.5], [22, 15]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"admin_country_z5-\",\n" +
                "      \"type\": \"line\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"boundary\",\n" +
                "      \"minzoom\": 5,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"<=\", \"admin_level\", 2],\n" +
                "        [\"==\", \"$type\", \"LineString\"]\n" +
                "      ],\n" +
                "      \"layout\": {\n" +
                "        \"line-cap\": \"round\",\n" +
                "        \"line-join\": \"round\",\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"line-color\": \"hsl(0, 0%, 60%)\",\n" +
                "        \"line-width\": {\"base\": 1.3, \"stops\": [[3, 0.5], [22, 15]]}\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"airport_label\",\n" +
                "      \"type\": \"symbol\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"aerodrome_label\",\n" +
                "      \"minzoom\": 10,\n" +
                "      \"filter\": [\"all\", [\"has\", \"iata\"], [\"!has\", \"name:en\"]],\n" +
                "      \"layout\": {\n" +
                "        \"icon-size\": 1,\n" +
                "        \"text-anchor\": \"top\",\n" +
                "        \"text-field\": \"{name:latin}\",\n" +
                "        \"text-font\": [\"Open Sans Regular\", \"Noto Serif Regular\"],\n" +
                "        \"text-max-width\": 8,\n" +
                "        \"text-offset\": [0, 0.5],\n" +
                "        \"text-size\": 11,\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"text-color\": \"#666\",\n" +
                "        \"text-halo-blur\": 1,\n" +
                "        \"text-halo-color\": \"rgba(255,255,255,0.75)\",\n" +
                "        \"text-halo-width\": 1\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"airport_label-en\",\n" +
                "      \"type\": \"symbol\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"aerodrome_label\",\n" +
                "      \"minzoom\": 10,\n" +
                "      \"filter\": [\"all\", [\"has\", \"iata\"], [\"has\", \"name:en\"]],\n" +
                "      \"layout\": {\n" +
                "        \"icon-size\": 1,\n" +
                "        \"text-anchor\": \"top\",\n" +
                "        \"text-field\": \"{name:en}\",\n" +
                "        \"text-font\": [\"Open Sans Regular\", \"Noto Serif Regular\"],\n" +
                "        \"text-max-width\": 8,\n" +
                "        \"text-offset\": [0, 0.5],\n" +
                "        \"text-size\": 11,\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"text-color\": \"#666\",\n" +
                "        \"text-halo-blur\": 1,\n" +
                "        \"text-halo-color\": \"rgba(255,255,255,0.75)\",\n" +
                "        \"text-halo-width\": 1\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"road_major_label\",\n" +
                "      \"type\": \"symbol\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation_name\",\n" +
                "      \"minzoom\": 13,\n" +
                "      \"filter\": [\"all\", [\"==\", \"$type\", \"LineString\"], [\"!has\", \"name:en\"]],\n" +
                "      \"layout\": {\n" +
                "        \"symbol-placement\": \"line\",\n" +
                "        \"text-field\": \"{name:latin}\",\n" +
                "        \"text-font\": [\"Open Sans Regular\", \"Noto Serif Regular\"],\n" +
                "        \"text-letter-spacing\": 0.1,\n" +
                "        \"text-rotation-alignment\": \"map\",\n" +
                "        \"text-size\": {\"base\": 1.4, \"stops\": [[10, 8], [20, 14]]},\n" +
                "        \"text-transform\": \"uppercase\",\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"text-color\": \"#000\",\n" +
                "        \"text-halo-color\": \"hsl(0, 0%, 100%)\",\n" +
                "        \"text-halo-width\": 2\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"road_major_label-en\",\n" +
                "      \"type\": \"symbol\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"transportation_name\",\n" +
                "      \"minzoom\": 13,\n" +
                "      \"filter\": [\"all\", [\"==\", \"$type\", \"LineString\"], [\"has\", \"name:en\"]],\n" +
                "      \"layout\": {\n" +
                "        \"symbol-placement\": \"line\",\n" +
                "        \"text-field\": \"{name:en}\",\n" +
                "        \"text-font\": [\"Open Sans Regular\", \"Noto Serif Regular\"],\n" +
                "        \"text-letter-spacing\": 0.1,\n" +
                "        \"text-rotation-alignment\": \"map\",\n" +
                "        \"text-size\": {\"base\": 1.4, \"stops\": [[10, 8], [20, 14]]},\n" +
                "        \"text-transform\": \"uppercase\",\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"text-color\": \"#000\",\n" +
                "        \"text-halo-color\": \"hsl(0, 0%, 100%)\",\n" +
                "        \"text-halo-width\": 2\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"place_label_other\",\n" +
                "      \"type\": \"symbol\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"place\",\n" +
                "      \"minzoom\": 8,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"Point\"],\n" +
                "        [\"!in\", \"class\", \"city\", \"state\", \"country\", \"continent\"],\n" +
                "        [\"!has\", \"name:en\"]\n" +
                "      ],\n" +
                "      \"layout\": {\n" +
                "        \"text-anchor\": \"center\",\n" +
                "        \"text-field\": \"{name:latin}\",\n" +
                "        \"text-font\": [\"Open Sans Regular\", \"Noto Serif Regular\"],\n" +
                "        \"text-max-width\": 6,\n" +
                "        \"text-size\": {\"stops\": [[6, 10], [12, 14]]},\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"text-color\": \"hsl(0, 0%, 25%)\",\n" +
                "        \"text-halo-blur\": 0,\n" +
                "        \"text-halo-color\": \"hsl(0, 0%, 100%)\",\n" +
                "        \"text-halo-width\": 2\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"place_label_other-en\",\n" +
                "      \"type\": \"symbol\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"place\",\n" +
                "      \"minzoom\": 8,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"Point\"],\n" +
                "        [\"!in\", \"class\", \"city\", \"state\", \"country\", \"continent\"],\n" +
                "        [\"has\", \"name:en\"]\n" +
                "      ],\n" +
                "      \"layout\": {\n" +
                "        \"text-anchor\": \"center\",\n" +
                "        \"text-field\": \"{name:en}\",\n" +
                "        \"text-font\": [\"Open Sans Regular\", \"Noto Serif Regular\"],\n" +
                "        \"text-max-width\": 6,\n" +
                "        \"text-size\": {\"stops\": [[6, 10], [12, 14]]},\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"text-color\": \"hsl(0, 0%, 25%)\",\n" +
                "        \"text-halo-blur\": 0,\n" +
                "        \"text-halo-color\": \"hsl(0, 0%, 100%)\",\n" +
                "        \"text-halo-width\": 2\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"place_label_city\",\n" +
                "      \"type\": \"symbol\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"place\",\n" +
                "      \"maxzoom\": 16,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"Point\"],\n" +
                "        [\"==\", \"class\", \"city\"],\n" +
                "        [\"!has\", \"name:en\"]\n" +
                "      ],\n" +
                "      \"layout\": {\n" +
                "        \"text-field\": \"{name:latin}\",\n" +
                "        \"text-font\": [\"Open Sans Regular\", \"Noto Serif Regular\"],\n" +
                "        \"text-max-width\": 10,\n" +
                "        \"text-size\": {\"stops\": [[3, 11], [8, 16]]}\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"text-color\": \"hsl(0, 0%, 0%)\",\n" +
                "        \"text-halo-blur\": 0,\n" +
                "        \"text-halo-color\": \"hsla(0, 0%, 100%, 0.75)\",\n" +
                "        \"text-halo-width\": 2\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"place_label_city-en\",\n" +
                "      \"type\": \"symbol\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"place\",\n" +
                "      \"maxzoom\": 16,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"Point\"],\n" +
                "        [\"==\", \"class\", \"city\"],\n" +
                "        [\"has\", \"name:en\"]\n" +
                "      ],\n" +
                "      \"layout\": {\n" +
                "        \"text-field\": \"{name:en}\",\n" +
                "        \"text-font\": [\"Open Sans Regular\", \"Noto Serif Regular\"],\n" +
                "        \"text-max-width\": 10,\n" +
                "        \"text-size\": {\"stops\": [[3, 11], [8, 16]]}\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"text-color\": \"hsl(0, 0%, 0%)\",\n" +
                "        \"text-halo-blur\": 0,\n" +
                "        \"text-halo-color\": \"hsla(0, 0%, 100%, 0.75)\",\n" +
                "        \"text-halo-width\": 2\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"country_label-other\",\n" +
                "      \"type\": \"symbol\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"place\",\n" +
                "      \"maxzoom\": 12,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"Point\"],\n" +
                "        [\"==\", \"class\", \"country\"],\n" +
                "        [\"!has\", \"iso_a2\"],\n" +
                "        [\"!has\", \"name:en\"]\n" +
                "      ],\n" +
                "      \"layout\": {\n" +
                "        \"text-field\": \"{name:latin}\",\n" +
                "        \"text-font\": [\"Open Sans Regular\", \"Noto Serif Regular\"],\n" +
                "        \"text-max-width\": 10,\n" +
                "        \"text-size\": {\"stops\": [[3, 12], [8, 22]]},\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"text-color\": \"hsl(0, 0%, 13%)\",\n" +
                "        \"text-halo-blur\": 0,\n" +
                "        \"text-halo-color\": \"rgba(255,255,255,0.75)\",\n" +
                "        \"text-halo-width\": 2\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"country_label-other-en\",\n" +
                "      \"type\": \"symbol\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"place\",\n" +
                "      \"maxzoom\": 12,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"Point\"],\n" +
                "        [\"==\", \"class\", \"country\"],\n" +
                "        [\"!has\", \"iso_a2\"],\n" +
                "        [\"has\", \"name:en\"]\n" +
                "      ],\n" +
                "      \"layout\": {\n" +
                "        \"text-field\": \"{name:en}\",\n" +
                "        \"text-font\": [\"Open Sans Regular\", \"Noto Serif Regular\"],\n" +
                "        \"text-max-width\": 10,\n" +
                "        \"text-size\": {\"stops\": [[3, 12], [8, 22]]},\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"text-color\": \"hsl(0, 0%, 13%)\",\n" +
                "        \"text-halo-blur\": 0,\n" +
                "        \"text-halo-color\": \"rgba(255,255,255,0.75)\",\n" +
                "        \"text-halo-width\": 2\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"country_label\",\n" +
                "      \"type\": \"symbol\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"place\",\n" +
                "      \"maxzoom\": 12,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"Point\"],\n" +
                "        [\"==\", \"class\", \"country\"],\n" +
                "        [\"has\", \"iso_a2\"],\n" +
                "        [\"!has\", \"name:en\"]\n" +
                "      ],\n" +
                "      \"layout\": {\n" +
                "        \"text-field\": \"{name:latin}\",\n" +
                "        \"text-font\": [\"Open Sans Regular\", \"Noto Serif Bold\"],\n" +
                "        \"text-max-width\": 10,\n" +
                "        \"text-size\": {\"stops\": [[3, 12], [8, 22]]},\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"text-color\": \"hsl(0, 0%, 13%)\",\n" +
                "        \"text-halo-blur\": 0,\n" +
                "        \"text-halo-color\": \"rgba(255,255,255,0.75)\",\n" +
                "        \"text-halo-width\": 2\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"country_label-en\",\n" +
                "      \"type\": \"symbol\",\n" +
                "      \"source\": \"openmaptiles\",\n" +
                "      \"source-layer\": \"place\",\n" +
                "      \"maxzoom\": 12,\n" +
                "      \"filter\": [\n" +
                "        \"all\",\n" +
                "        [\"==\", \"$type\", \"Point\"],\n" +
                "        [\"==\", \"class\", \"country\"],\n" +
                "        [\"has\", \"iso_a2\"],\n" +
                "        [\"has\", \"name:en\"]\n" +
                "      ],\n" +
                "      \"layout\": {\n" +
                "        \"text-field\": \"{name:en}\",\n" +
                "        \"text-font\": [\"Open Sans Regular\", \"Noto Serif Bold\"],\n" +
                "        \"text-max-width\": 10,\n" +
                "        \"text-size\": {\"stops\": [[3, 12], [8, 22]]},\n" +
                "        \"visibility\": \"visible\"\n" +
                "      },\n" +
                "      \"paint\": {\n" +
                "        \"text-color\": \"hsl(0, 0%, 13%)\",\n" +
                "        \"text-halo-blur\": 0,\n" +
                "        \"text-halo-color\": \"rgba(255,255,255,0.75)\",\n" +
                "        \"text-halo-width\": 2\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"id\": \"18feedc0-4957-4eab-ad52-0629bc0e5f3e\"\n" +
                "}";
    }
}
