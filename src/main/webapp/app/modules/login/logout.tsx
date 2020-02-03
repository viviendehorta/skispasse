import React from 'react';
import { connect } from 'react-redux';

import { IRootState } from 'app/config/root.reducer';
import { logout } from 'app/components/authentication/authentication.reducer';

export interface ILogoutProps extends StateProps, DispatchProps {
  idToken: string;
  logoutUrl: string;
}

export class Logout extends React.Component<ILogoutProps> {

  render() {
    const logoutUrl = this.props.logoutUrl;
    if (logoutUrl) {
      // if Keycloak, logoutUrl has protocol/openid-connect in it
      window.location.href = logoutUrl.includes('/protocol')
        ? logoutUrl + '?redirect_uri=' + window.location.origin
        : logoutUrl + '?id_token_hint=' + this.props.idToken + '&post_logout_redirect_uri=' + window.location.origin;
    }

    return (
      <div className="p-5">
        <h4>Logged out successfully!</h4>
      </div>
    );
  }

  componentDidMount() {
    this.props.logout();
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  logoutUrl: storeState.authentication.logoutUrl,
  idToken: storeState.authentication.idToken
});

const mapDispatchToProps = { logout };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Logout);
