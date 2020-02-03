import React from 'react';
import { shallow } from 'enzyme';

import sinon from 'sinon';

import LoadingBar from 'react-redux-loading-bar';

import { Home, Brand } from 'app/shared/layout/menu/menu-components';
import { AdminMenu, AccountMenu, LocaleMenu } from 'app/shared/layout/menu';
import Menu from 'app/shared/layout/menu/menu';

describe('Menu', () => {
  let mountedWrapper;

  const localeSpy = sinon.spy();

  const devProps = {
    isAuthenticated: true,
    isAdmin: true,
    currentLocale: 'en',
    onLocaleChange: localeSpy,
    isInProduction: false,
    isSwaggerEnabled: true
  };
  const prodProps = {
    ...devProps,
    isInProduction: true,
    isSwaggerEnabled: false
  };
  const userProps = {
    ...prodProps,
    isAdmin: false
  };
  const guestProps = {
    ...prodProps,
    isAdmin: false,
    isAuthenticated: false
  };

  const wrapper = (props = devProps) => {
    if (!mountedWrapper) {
      mountedWrapper = shallow(<Menu {...props} />);
    }
    return mountedWrapper;
  };

  beforeEach(() => {
    mountedWrapper = undefined;
  });

  // All tests will go here
  it('Renders a Menu component in dev profile with LoadingBar and Nav.', () => {
    const component = wrapper();
    // the created snapshot must be committed to source control
    expect(component).toMatchSnapshot();
    expect(component.find(LoadingBar).length).toEqual(1);
    const menu = component.find('.menu');
    expect(menu.length).toEqual(1);
    expect(menu.find(Brand).length).toEqual(1);
    const nav = component.find('.nav');
    expect(nav.length).toEqual(1);
    expect(nav.find(Home).length).toEqual(1);
    expect(nav.find(AdminMenu).length).toEqual(1);
    expect(nav.find(LocaleMenu).length).toEqual(1);

    expect(nav.find(AccountMenu).length).toEqual(1);
  });

  it('Renders a Menu component in prod profile with LoadingBar and Nav.', () => {
    const component = wrapper(prodProps);
    // the created snapshot must be committed to source control
    expect(component).toMatchSnapshot();
    const menu = component.find('.menu');
    expect(menu.length).toEqual(1);
    expect(menu.find(Brand).length).toEqual(1);
    const nav = component.find('.nav');
    expect(nav.length).toEqual(1);
    expect(nav.find(Home).length).toEqual(1);
    expect(nav.find(AdminMenu).length).toEqual(1);
    expect(nav.find(LocaleMenu).length).toEqual(1);

    expect(nav.find(AccountMenu).length).toEqual(1);
  });

  it('Renders a Menu component in prod profile with logged in User', () => {
    const nav = wrapper(userProps).find('.nav');
    expect(nav.find(AdminMenu).length).toEqual(0);
    const account = nav.find(AccountMenu);
    expect(account.first().props().isAuthenticated).toEqual(true);
  });

  it('Renders a Menu component in prod profile with no logged in User', () => {
    const nav = wrapper(guestProps).find('.nav');
    expect(nav.find(AdminMenu).length).toEqual(0);
    const account = nav.find(AccountMenu);
    expect(account.length).toEqual(1);
    expect(account.first().props().isAuthenticated).toEqual(false);
  });

  it('Renders an uncollapsed Menu component', () => {
    const nav = wrapper(devProps).find('.nav');
    expect(nav.hasClass('collapsed')).toBeFalsy();
  });
});
