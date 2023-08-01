import { useLocation } from 'react-router-dom';

export default function Home() {
    const location = useLocation();
    console.log("location,", location)
    return (
        <div id="knife4j-home-page">
            <h1>我是主页!</h1>
            <p>我是主页的介绍信息</p>
            <p>
            </p>
        </div>
    );
}