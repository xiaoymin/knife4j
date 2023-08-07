import { createBrowserRouter } from "react-router-dom";
import App from '../App.tsx'

import Home from "../pages/Home.tsx";
import Schema from "../pages/Schema.tsx";
import Authorize from "../pages/Authorize.tsx";
import GlobalParam from "../pages/document/GlobalParam.tsx";
import OfficeDoc from "../pages/document/OfficeDoc.tsx";
import Settings from "../pages/document/Settings.tsx";

import ApiHome from "../pages/api/ApiHome.tsx";

const router = createBrowserRouter([
    {
        path: "/",
        element: <App />,
        children: [
            {
                path: ":group/home",
                element: <Home />
            },
            {
                path: ":group/schema",
                element: <Schema />
            },
            {
                path: ":group/authorize",
                element: <Authorize />
            },
            {
                path: ":group/globalParam",
                element: <GlobalParam />
            },
            {
                path: ":group/Officdoc",
                element: <OfficeDoc />
            },
            {
                path: ":group/Settings",
                element: <Settings />
            },
            {
                path: ":group/:tag/:operaterId",
                element: <ApiHome />
            }
        ],
    }, {
        path: "test",
        element: <Home />
    }
]);

export default router