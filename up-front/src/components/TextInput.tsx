import { InputHTMLAttributes } from "react";

interface TextInputProps extends InputHTMLAttributes<HTMLInputElement> {
    label?: string;
}

export default function TextInput({ label, id, ...props }: TextInputProps) {
    return(
        <div className="flex flex-col">
            {label && (
                <label htmlFor={id} className="text-h5">
                    {label}
                </label>
            )}
            <input id={id} {...props} className="inline-flex items-center justify center border-1 border-black rounded-sm-custom p-1"/>
        </div>
    )
}