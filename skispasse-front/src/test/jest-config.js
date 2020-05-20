module.exports = {
  preset: "jest-preset-angular",
  transform: {
    "^.+\\.(ts|js|html)$": "ts-jest"
  },
  setupFilesAfterEnv: [
    "<rootDir>/setup-jest.ts"
  ],
  globals: {
    "ts-jest": {
      tsConfig: "<rootDir>/tsconfig-test.json",
      stringifyContentPathRegex: "\\.html$"
    }
  },
  testMatch: [
    "**/?(*.)+(spec|test).[jt]s?(x)"
  ]
};
