import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Redirect, RouteComponentProps } from 'react-router-dom';

import { IRootState } from 'app/components/reducers/root.reducer';
import { login } from 'app/components/reducers/authentication.reducer';
import LoginModal from './login-modal';

export interface ILoginProps extends StateProps, DispatchProps, RouteComponentProps<{}> {}

export const Login = (props: ILoginProps) => {
  const [showModal, setShowModal] = useState(props.showModal);

  useEffect(() => {
    setShowModal(true);
  }, []);

  const handleLogin = (username, password, rememberMe = false) => props.login(username, password, rememberMe);

  const handleClose = () => {
    setShowModal(false);
    props.history.push('/');
  };

  const { location, isAuthenticated } = props;
  const { from } = location.state || { from: { pathname: '/', search: location.search } };
  if (isAuthenticated) {
    return <Redirect to={from} />;
  }
  return <LoginModal showModal={showModal} handleLogin={handleLogin} handleClose={handleClose} loginError={props.loginError} />;
};

const mapStateToProps = ({ authentication }: IRootState) => ({
  isAuthenticated: authentication.isAuthenticated,
  loginError: authentication.loginError,
  showModal: authentication.showModalLogin
});

const mapDispatchToProps = { login };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Login);
