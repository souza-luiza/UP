interface EyeClosedProps {
    primaryColor: boolean; // se nao, eh a secondary color
}

export default function EyeClosed({ primaryColor }: EyeClosedProps) {
    return (
        <div className="inline-flex justify-center items-center">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="22" viewBox="0 0 25 20" fill="none">
                <path d="M17.94 16.94C16.2306 18.243 14.1491 18.9649 12 19C5 19 1 11 1 11C2.24389 8.68189 3.96914 6.6566 6.06 5.05999M9.9 3.23999C10.5883 3.07887 11.2931 2.99833 12 2.99999C19 2.99999 23 11 23 11C22.393 12.1356 21.6691 13.2047 20.84 14.19M14.12 13.12C13.8454 13.4147 13.5141 13.6511 13.1462 13.8151C12.7782 13.9791 12.3809 14.0673 11.9781 14.0744C11.5753 14.0815 11.1752 14.0074 10.8016 13.8565C10.4281 13.7056 10.0887 13.481 9.80385 13.1961C9.51897 12.9113 9.29439 12.5719 9.14351 12.1984C8.99262 11.8248 8.91853 11.4247 8.92563 11.0219C8.93274 10.6191 9.02091 10.2218 9.18488 9.85384C9.34884 9.48584 9.58525 9.15464 9.88 8.88M1 0L23 22" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
        </div>
    );

}