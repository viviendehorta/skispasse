import {definePreset} from "@primeng/themes";
import Lara from '@primeng/themes/lara';

export const primengPreset = definePreset(Lara, {
  semantic: {
    primary: {
      50: "{blue.100}",
      100: "{blue.300}",
      200: "{blue.300}",
      300: "{blue.400}",
      400: "{blue.500}",
      500: "{blue.600}",
      600: "{blue.700}",
      700: "{blue.800}",
      800: "{blue.900}",
      900: "{blue.950}",
      950: "{blue.950}"
    },
  }
});
